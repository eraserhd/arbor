(ns net.eraserhead.arbor.webui
  (:require
   [net.eraserhead.arbor.webui.bluetooth :as bt]
   [net.eraserhead.arbor.webui.events]
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [re-frame.core :as rf]
   ["@fortawesome/fontawesome-free/js/all.js"]))

(defn- legend []
  [:div.floating-card.legend
   [:h1 "Legend"]
   [:ul
    [:li "one"]
    [:li "two"]
    [:li "three"]]])

(defn- add-datum-command []
  [:button.icon [:i.fa-solid.fa-plus]])

(defn- settings-command []
  (let [dialog (r/atom nil)]
    (fn settings-command* []
      [:<>
       [:button.icon {:on-click #(.showModal @dialog)}
        [:i.fa-solid.fa-gear]]
       [:dialog.settings {:ref #(reset! dialog %),
                          :closedby "any"}
        [:h1 "Settings"]
        [:h2 "Machines"]]])))

(defn- command-bar []
  [:div.floating-card.command-bar
   [add-datum-command]
   [settings-command]])

(defn- arbor []
  [:<>
   [legend]
   [command-bar]])

(defonce root (rdc/create-root (js/document.getElementById "app")))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [:initialize])
  (rdc/render root [arbor]))
