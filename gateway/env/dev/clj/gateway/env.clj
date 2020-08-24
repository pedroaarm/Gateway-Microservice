(ns gateway.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [gateway.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[gateway started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[gateway has shut down successfully]=-"))
   :middleware wrap-dev})
