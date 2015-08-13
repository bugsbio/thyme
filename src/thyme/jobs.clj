(ns thyme.jobs
  (:require
    [amazonica.aws.sns                    :as sns]
    [clojurewerkz.quartzite.scheduler     :as scheduler]
    [clojurewerkz.quartzite.triggers      :as trigger]
    [clojurewerkz.quartzite.jobs          :as job  :refer [defjob]]
    [clojurewerkz.quartzite.schedule.cron :as cron :refer [schedule cron-schedule]]))

(defjob SNSNotificationJob
  [ctx]
  (println "Running job"))

(defn new-trigger
  [id cron-expression]
  (trigger/build
    (trigger/with-identity (trigger/key id))
    (trigger/start-now)
    (trigger/with-schedule (schedule (cron-schedule cron-expression)))))

(defn new-job
  [id]
  (job/build
    (job/of-type SNSNotificationJob)
    (job/with-identity (job/key id))))

(defn schedule-all
  [scheduler]
  (scheduler/schedule scheduler
                      (new-job "jobs.test")
                      (new-trigger "triggers.test" "0 * * * * ?")))
