(ns thyme.jobs.sns
  (:require
    [thyme.util                  :as u]
    [amazonica.aws.sns           :as sns]
    [clojurewerkz.quartzite.jobs :as job :refer [defjob]]
    [clj-airbrake.core           :as airbrake]
    [environ.core                :refer [env]]
    [taoensso.timbre             :as log]))

(defjob SNSNotificationJob
  [ctx]
  (try
    (let [{:keys [name topic endpoint payload] :or {endpoint "eu-west-1"}} ;; selfishly making the region we use the default
          (u/extract-manifest ctx)
          {:keys [topic-arn]}
          (sns/create-topic {:endpoint endpoint} :name topic)]
      (log/info "Running" name)
      (sns/publish {:endpoint endpoint}
                   :topic-arn topic-arn
                   :subject name
                   :message (pr-str payload)))
    (catch Throwable t
      (log/error t "Error running job")
      (when-let [airbrake-api-key (env :airbrake-api-key)]
        (airbrake/notify airbrake-api-key
                         (env :app-env)
                         (env :user-dir)
                         (ex-info (.getMessage t) (u/extract-manifest ctx) t)))
      (throw t))))
