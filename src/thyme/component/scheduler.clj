(ns thyme.component.scheduler
  (:require
    [thyme.jobs                       :as jobs]
    [taoensso.timbre                  :as log]
    [com.stuartsierra.component       :as component]
    [clojurewerkz.quartzite.scheduler :as scheduler]))

(defrecord Scheduler
  [job-dir]

  component/Lifecycle
  (start [this]
    (log/info "Starting Scheduler for jobs in" job-dir)
    (assoc this :instance
           (-> (scheduler/initialize)
               (scheduler/start)
               (jobs/schedule-all job-dir))))
  (stop [this]
    (log/info "Stopping Scheduler")
    (-> (:instance this)
        (scheduler/shutdown))
    (dissoc this :instance)))

(defn new-scheduler
  [job-dir]
  (Scheduler. job-dir))
