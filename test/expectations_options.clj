(ns expectations-options
  (:require
    [taoensso.timbre :as log]))

(defn
  ^{:expectations-options :before-run}
  disable-logging
  []
  (log/set-level! :fatal))
