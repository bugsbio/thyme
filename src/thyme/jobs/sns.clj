(ns thyme.jobs.sns
  (:require
    [thyme.macros      :refer [defjob]]
    [amazonica.aws.sns :as sns]))

(defjob SNSNotificationJob
  [{:keys [name topic endpoint payload] :or {endpoint "eu-west-1"}}]
  (let [{:keys [topic-arn]}
        (sns/create-topic {:endpoint endpoint} :name topic)]
    (sns/publish {:endpoint endpoint}
                 :topic-arn topic-arn
                 :subject name
                 :message (pr-str payload))))
