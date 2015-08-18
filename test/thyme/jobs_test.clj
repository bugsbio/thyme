(ns thyme.jobs-test
  (:require
    [thyme.jobs                       :as j]
    [expectations                     :refer :all]
    [clojurewerkz.quartzite.scheduler :as scheduler]
    [clojure.java.io                  :as io]))

;;;;;;; new-trigger ;;;;;;

(expect AssertionError (j/new-trigger {}))
(expect AssertionError (j/new-trigger {:id 123}))
(expect AssertionError (j/new-trigger {:id "trigger.123" :schedule nil}))

(expect org.quartz.Trigger
        (-> {:id "trigger.123" :schedule "0 * * * * ?"} j/new-trigger))

(expect "0 * * * * ?"
        (-> {:id "trigger.123" :schedule "0 * * * * ?"} j/new-trigger .getCronExpression))

(expect "trigger.123"
        (-> {:id "trigger.123" :schedule "0 * * * * ?"} j/new-trigger .getName))

;;;;;; new-job ;;;;;;

(expect AssertionError (j/new-job {}))
(expect AssertionError (j/new-job {:id "job.123" :type :not-a-job-type}))

(expect org.quartz.JobDetail
        (-> {:id "job.123" :type :sns} j/new-job))
(expect thyme.jobs.sns.SNSNotificationJob
        (-> {:id "job.123" :type :sns} j/new-job .getJobClass))

;;;;;; schedule-all ;;;;;;

(expect AssertionError (j/schedule-all nil nil))

(expect [[{:name "Thyme Test Job"
           :id "jobs.test"
           :type :sns
           :topic "test-thyme"
           :trigger {:id "triggers.test" :schedule "0 * * * * ?"}
           :payload {:foo "bar" :baz 123}}
          {:id "triggers.test" :schedule "0 * * * * ?"}]]

        (let [scheduler (reify org.quartz.Scheduler)
              jobs      (atom [])]
          (with-redefs [scheduler/schedule #(swap! jobs conj [%2 %3])
                        j/new-job          identity
                        j/new-trigger      identity]
            (j/schedule-all scheduler "test-jobs")
            (deref jobs))))
