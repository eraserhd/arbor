(ns net.eraserhead.arbor
  (:require
   [reagent.dom.client :as rdc]
   [re-frame.core :as rf]))

(defonce root (rdc/create-root (js/document.getElementById "app")))

(defn- arbor []
  [:ul
   [:li "hello"]])

(defn ^:dev/after-load start []
  (rdc/render root [arbor]))
