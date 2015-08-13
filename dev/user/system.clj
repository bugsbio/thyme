(ns user.system
  (:require
    [clojure.tools.namespace.repl :refer [disable-reload!]]))

(disable-reload! *ns*)

(def system (atom nil))
