(ns thyme.util-test
  (:require
    [thyme.jobs      :as j]
    [thyme.util      :as u]
    [expectations    :refer :all]
    [clojure.java.io :as io]))

(expect true  (u/edn? (io/file "test-dir/test-job.edn")))
(expect false (u/edn? (io/file "src/thyme/util.clj")))

(let [manifest {:name "Thyme Test Job"
                :id "jobs.test"
                :type :sns
                :topic "test-thyme"
                :trigger {:id "triggers.test" :schedule "0 * * * * ?"}
                :payload {:foo "bar" :baz 123}}]
(expect manifest
        (-> (j/new-job manifest)
            (.getJobDataMap)
            (u/extract-manifest))))
