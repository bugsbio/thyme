(defproject thyme "0.1.0-SNAPSHOT"
  :description "Schedule firing of SNS notifications"
  :url "http://github.com/bugsbio/thyme"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [amazonica "0.3.19"]
                 [com.taoensso/timbre "3.4.0"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [clj-airbrake "2.4.3"]
                 [clj-http-lite  "0.3.0"]
                 [cheshire "5.5.0"]
                 [environ "1.0.0"]
                 [miscellany "0.0.3"]
                 [com.stuartsierra/component "0.2.3"]]
  :main ^:skip-aot thyme.core
  :resource-paths ["resources" "jobs"]
  :target-path "target/%s"
  :plugins [[lein-expectations "0.0.8"]]
  :profiles {:dev {:source-paths ["src" "dev"]
                   :repl-options {:init-ns user}
                   :dependencies [[org.clojure/tools.namespace "0.2.7"]
                                  [expectations "2.1.2"]]}
             :uberjar {:aot :all}})
