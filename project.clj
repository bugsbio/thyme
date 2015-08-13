(defproject thyme "0.1.0-SNAPSHOT"
  :description "Schedule firing of SNS notifications"
  :url "http://github.com/bugsbio/thyme"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [amazonica "0.3.19"]
                 [com.taoensso/timbre "3.4.0"]
                 [clj-airbrake "2.4.3"]
                 [environ "1.0.0"]
                 [com.stuartsierra/component "0.2.3"]]
  :main ^:skip-aot thyme.core
  :resource-paths ["jobs"]
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["src" "dev"]
                   :repl-options {:init-ns user}
                   :dependencies [[org.clojure/tools.namespace "0.2.7"]
                                  [expectations "2.1.2"]]}
             :uberjar {:aot :all}})
