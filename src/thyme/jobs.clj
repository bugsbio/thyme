(ns thyme.jobs
  (:require
    [clj-airbrake.core                    :as airbrake]
    [amazonica.aws.sns                    :as sns]
    [clojure.edn                          :as edn]
    [clojure.java.io                      :as io]
    [environ.core                         :refer [env]]
    [taoensso.timbre                      :as log]
    [clojurewerkz.quartzite.conversion    :as conversion]
    [clojurewerkz.quartzite.schedule.cron :as cron]
    [clojurewerkz.quartzite.scheduler     :as scheduler]
    [clojurewerkz.quartzite.triggers      :as trigger]
    [clojurewerkz.quartzite.jobs          :as job :refer [defjob]]))

(defn- extract-manifest
  [ctx]
  (-> (conversion/from-job-data ctx)
      (get "manifest")
      (edn/read-string)))

(defjob SNSNotificationJob
  [ctx]
  (try
    (let [{:keys [name topic endpoint payload] :or {endpoint "eu-west-1"}} ;; selfishly making the region we use the default
          (extract-manifest ctx)
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
                         (ex-info (.getMessage t) (extract-manifest ctx) t)))
      (throw t))))

(defn new-trigger
  [{:keys [id schedule]}]
  (trigger/build
    (trigger/with-identity (trigger/key id))
    (trigger/start-now)
    (trigger/with-schedule (cron/schedule (cron/cron-schedule schedule)))))

(defn new-job
  [{:keys [id] :as manifest}]
  (job/build
    (job/of-type SNSNotificationJob)
    (job/using-job-data {"manifest" (pr-str manifest)})
    (job/with-identity (job/key id))))

(defn- edn?
  [f]
  (.endsWith (.getName f) ".edn"))

(defn schedule-all
  [scheduler job-dir]
  (doseq [f (file-seq (io/file job-dir))
          :when (edn? f)
          :let [{:keys [name trigger] :as manifest} (edn/read-string (slurp f))]]
    (log/info "Scheduling" name "with schedule" (:schedule trigger))
    (scheduler/schedule scheduler
                        (new-job manifest)
                        (new-trigger trigger))))
