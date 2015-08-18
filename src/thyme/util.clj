(ns thyme.util
  (:require
    [clojure.edn                       :as edn]
    [clojurewerkz.quartzite.conversion :as conversion]))

(defn edn?
  [f]
  (.endsWith (.getName f) ".edn"))

(defn extract-manifest
  [ctx]
  (-> (conversion/from-job-data ctx)
      (get "manifest")
      (edn/read-string)))
