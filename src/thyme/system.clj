(ns thyme.system
  (:require
    [thyme.component.scheduler  :refer [new-scheduler]]
    [com.stuartsierra.component :refer [system-map using]]))

(defn new-system
  [job-dir]
  (system-map
    :scheduler (new-scheduler job-dir)))
