(ns thyme.test-helpers
  (:require
    [com.stuartsierra.component :as component]))

(defmacro with-system
  [[binding system] & forms]
  `(let [~binding (component/start ~system)]
     (let [retval# ~@forms]
       (component/stop ~binding)
       retval#)))
