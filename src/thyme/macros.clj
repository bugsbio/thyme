(ns thyme.macros
  (:require
    [thyme.util                  :as u]
    [clj-airbrake.core           :as airbrake]
    [clojurewerkz.quartzite.jobs :as job]
    [environ.core                :as environ]
    [taoensso.timbre             :as log]))

(defmacro defjob
  [name [manifest-binding] & body]
  `(job/defjob ~name [ctx#]
     (try
       (let [manifest#         (u/extract-manifest ctx#)
             ~manifest-binding manifest#]
         (log/info "Running" (:name manifest#))
         ~@body
         (log/info "Completed" (:name manifest#)))
       (catch Throwable t#
         (log/error t# "Error running job")
         (when-let [airbrake-api-key# (environ/env :airbrake-api-key)]
           (airbrake/notify airbrake-api-key#
                            (environ/env :app-env)
                            (environ/env :user-dir)
                            (ex-info (.getMessage t#) (u/extract-manifest ctx#))))
         (throw t#)))))
