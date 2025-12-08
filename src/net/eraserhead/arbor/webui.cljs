(ns net.eraserhead.arbor.webui
  (:require
   [reagent.dom.client :as rdc]
   [re-frame.core :as rf]))

(defn- arbor []
  [:ul
   [:li "hello"]])

(defonce root (rdc/create-root (js/document.getElementById "app")))

(defn ^:dev/after-load start []
  (rdc/render root [arbor]))
