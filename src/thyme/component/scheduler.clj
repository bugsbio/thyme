(ns thyme.component.scheduler
  (:require
    [thyme.jobs                       :as jobs]
    [com.stuartsierra.component       :as component]
    [clojurewerkz.quartzite.scheduler :as scheduler]))

(defrecord Scheduler
  [job-dir]

  component/Lifecycle
  (start [this]
    (assoc this :instance
           (-> (scheduler/initialize)
               (scheduler/start)
               (doto (jobs/schedule-all)))))
  (stop [this]
    (-> (:instance this)
        (scheduler/shutdown))
    (dissoc this :instance)))

(defn new-scheduler
  [job-dir]
  (Scheduler. job-dir))
