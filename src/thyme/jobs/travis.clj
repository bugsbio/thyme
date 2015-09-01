(ns thyme.jobs.travis
  (:require
    [thyme.macros         :refer [defjob]]
    [clj-http.lite.client :as http]
    [environ.core         :refer [env]]
    [cheshire.core        :as json]))

(defn url
  [org repo pro?]
  (str "https://api.travis-ci." (if pro? "com" "org") "/repo/" org "%2F" repo "/requests"))

(defn headers
  []
  {"Content-Type" "application/json"
   "Accept" "application/json"
   "Travis-API-Version" "3"
   "Authorization" (str "token " (env :travis-api-token))})

(defn body
  []
  (json/generate-string {:request {:branch "master"}}))

(defjob TravisBuildJob
  [{:keys [org repo pro?]}]
  (http/post (url org repo pro?) {:headers (headers) :body (body)}))
