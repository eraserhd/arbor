(ns cadro.ui.legend
  (:require
   [cadro.db :as db]
   [cadro.model.locus :as locus]
   [cadro.model.object :as object]
   [re-frame.core :as rf]
   [re-posh.core :as re-posh]
   ["@fortawesome/fontawesome-free/js/all.js"]))

(re-posh/reg-sub
 ::loci-tree
 (fn [_ _]
   {:type  :query
    :query '[:find [?id ...]
             :where [?id ::locus/offset]]}))

(re-posh/reg-sub
 ::locus
 (fn [_ [_ eid]]
   {:type    :pull
    :pattern '[::object/id
               ::object/display-name]
    :id      eid}))

(re-posh/reg-event-ds
 ::new-machine
 (fn [ds _]
   [{::object/id           (random-uuid)
     ::object/display-name "New Machine"
     ::locus/offset        {"x" 42}}]))

(defn legend-key [eid]
  (let [{:keys [::object/id ::object/display-name]} @(re-posh/subscribe [::locus eid])]
    [:li display-name]))

(defn legend []
  [:div.floating-card.legend
   [:h1 "Legend"]
   (into [:ul]
         (map (fn [dbid]
                ^{:key (str dbid)}
                [legend-key dbid]))
         @(re-posh/subscribe [::loci-tree]))
   [:button.icon.new-machine
    {:on-click #(rf/dispatch [::new-machine])}
    [:i.fa-solid.fa-plug-circle-plus]]])
