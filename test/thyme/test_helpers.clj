(ns thyme.test-helpers)

(defmacro with-system
  [[binding system] & forms]
  `(let [~binding (com.stuartsierra.component/start ~system)]
     ~@forms
     (com.stuartsierra.component/stop ~binding)))
