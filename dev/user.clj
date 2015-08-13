(ns user
  (:require
    [user.system                  :refer :all]
    [thyme.system                 :refer [new-system]]
    [com.stuartsierra.component   :as component]
    [taoensso.timbre              :as log]
    [clojure.tools.namespace.repl :as repl]))

(defn init []
  (log/set-level! :debug)
  (log/info "*************************************************************")
  (log/info "HELLO.")
  (log/info "IT'S THYME.")
  (log/info "*************************************************************")
  (reset! system (new-system "jobs")))

(defn start []
  (swap! system component/start)
  (log/info "The system has started."))

(defn stop []
  (swap! system (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (repl/refresh :after 'user/go))
