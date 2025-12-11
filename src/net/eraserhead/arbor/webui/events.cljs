(ns net.eraserhead.arbor.webui.events
  (:require
   [net.eraserhead.arbor :as arbor]
   [re-frame.core :as rf]))

(rf/reg-event-db
 ::initialize
 (constantly arbor/initial-state))
