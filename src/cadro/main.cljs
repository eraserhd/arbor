(ns cadro.main
  (:require
   [cadro.db]
   [clojure.spec.alpha :as s]
   [reagent.dom.client :as rdc]
   [re-posh.core :as rp]))

(defonce root (rdc/create-root (js/document.getElementById "app")))

(defn ^:dev/after-load start []
  (when ^boolean goog.DEBUG
    (s/check-asserts true))
  (rdc/render root [:h1 "hello, world"]))
