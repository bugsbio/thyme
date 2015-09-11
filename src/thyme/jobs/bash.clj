(ns thyme.jobs.bash
  (:require
    [thyme.macros    :refer [defjob]]
    [taoensso.timbre :as log]
    [me.raynes.conch :refer [with-programs]]))

(defjob BashScriptJob
  [{:keys [script args]}]
  (with-programs [bash]
    (doseq [out (apply bash script (conj args {:seq true}))]
      (log/info out))))
