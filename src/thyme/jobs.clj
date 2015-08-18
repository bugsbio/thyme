(ns thyme.jobs
  (:require
    [thyme.util                           :as u]
    [amazonica.aws.sns                    :as sns]
    [clojure.java.io                      :as io]
    [environ.core                         :refer [env]]
    [taoensso.timbre                      :as log]
    [clojurewerkz.quartzite.schedule.cron :as cron]
    [clojurewerkz.quartzite.scheduler     :as scheduler]
    [clojurewerkz.quartzite.triggers      :as trigger]
    [clojurewerkz.quartzite.jobs          :as job :refer [defjob]]))

(def job-classes
  "Map of keywords to job classes used when reading job manifests."
  {:sns thyme.jobs.sns.SNSNotificationJob})

(defn new-trigger
  "Create a new Cron trigger with the specified schedule."
  ^org.quartz.Trigger
  [{:keys [id schedule]}]
  {:pre [(string? id)
         (string? schedule)]}
  (trigger/build
    (trigger/with-identity (trigger/key id))
    (trigger/start-now)
    (trigger/with-schedule (cron/schedule (cron/cron-schedule schedule)))))

(defn new-job
  "Create a Quartz job as specified by the provided job manifest."
  ^org.quartz.Job
  [{:keys [id type] :as manifest}]
  {:pre [(string? id)
         (contains? job-classes type)]}
  (job/build
    (job/of-type (get job-classes type))
    (job/using-job-data {"manifest" (pr-str manifest)})
    (job/with-identity (job/key id))))

(defn schedule-all
  "Given an org.quartz.Scheduler and a directory containing job manifests,
  creates and schedules jobs as specified by the EDN files in the job directory."
  ^org.quartz.Scheduler
  [^org.quartz.Scheduler scheduler job-dir]
  {:pre [(string? job-dir)]}
  (doseq [f (file-seq (io/file job-dir))
          :when (u/edn? f)
          :let [{:keys [name trigger] :as manifest} (edn/read-string (slurp f))]]
    (log/info "Scheduling" name "with schedule" (:schedule trigger))
    (scheduler/schedule scheduler
                        (new-job manifest)
                        (new-trigger trigger)))
  scheduler)
