(ns thyme.core
  (:require
    [thyme.system               :refer [new-system]]
    [miscellany.lifecycle       :refer [on-shutdown]]
    [taoensso.timbre            :as log]
    [com.stuartsierra.component :as component])
  (:gen-class))

(defn- add-shutdown-hook
  [system]
  (on-shutdown (component/stop system)))

(defn -main
  [& [job-dir]]
  (log/info "*************************************************************")
  (log/info "HELLO.")
  (log/info "IT'S THYME.")
  (log/info "*************************************************************")
  (-> (new-system (or job-dir "jobs"))
      (component/start)
      (add-shutdown-hook)))
