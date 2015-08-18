(ns thyme.component.scheduler-test
  (:require
    [thyme.component.scheduler        :as s]
    [thyme.system                     :refer [new-system]]
    [thyme.test-helpers               :refer [with-system]]
    [com.stuartsierra.component       :as component]
    [clojurewerkz.quartzite.scheduler :as scheduler]
    [expectations                     :refer :all])
  (:import [org.quartz Scheduler]))

;; job-dir can be configured
(expect "jobs"
        (with-system [system (new-system "jobs")]
          (get-in system [:scheduler :job-dir])))

(expect "tasks"
        (with-system [system (new-system "tasks")]
          (get-in system [:scheduler :job-dir])))

;; The scheduler component creates an org.quartz.Scheduler
(expect org.quartz.Scheduler
        (with-system [system (new-system "test-jobs")]
          (get-in system [:scheduler :instance])))

;; The scheduler component shuts down the scheduler instance on shutdown
(expect scheduler/shutdown?
        (with-system [system (new-system "test-jobs")]
          (let [instance (get-in system [:scheduler :instance])]
            (component/stop system)
            instance)))

;; The scheduler component dissocs the scheduler instance on shutdown
(expect nil
        (with-system [system (new-system "test-jobs")]
          (let [shutdown-system (component/stop system)]
            (get-in shutdown-system [:scheduler :instance]))))
