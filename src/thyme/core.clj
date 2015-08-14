(ns thyme.core
  (:require
    [thyme.system               :refer [new-system]]
    [miscellany.lifecycle       :refer [on-shutdown]]
    [com.stuartsierra.component :as component])
  (:gen-class))

(defn- add-shutdown-hook
  [system]
  (on-shutdown (component/stop system)))

(defn -main
  [job-dir]
  (-> (new-system job-dir)
      (component/start)
      (add-shutdown-hook)))
