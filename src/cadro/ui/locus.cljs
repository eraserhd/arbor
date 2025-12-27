(ns cadro.ui.locus
  (:require
   [cadro.model.object :as object]
   [cadro.model.scale :as scale]
   [cadro.model.scale-controller :as scale-controller]
   [cadro.ui.input :as input]
   [cadro.ui.panel :as panel]
   [reagent.core :as ra]
   [re-frame.core :as rf]
   [re-posh.core :as re-posh]))

(def ^:private locus-to-edit (ra/atom nil))

(re-posh/reg-sub
 ::scale-eids
 (fn []
   {:type :query
    :query '[:find [?id ...]
             :where [?id ::scale-controller/address]]}))

(def scale-pull
  '[::object/id
    ::object/display-name
    ::scale-controller/address
    {::scale/_device [::object/id
                      ::object/display-name
                      ::scale/raw-value]}])

(re-posh/reg-sub
 ::scales
 :<- [::scale-eids]
 (fn [eids _]
   {:type    :pull-many
    :pattern scale-pull
    :ids      eids}))

(defn edit-panel []
  (when-let [eid @locus-to-edit]
    ^{:key (str eid)}
    [panel/panel {:title "Edit Locus"
                  :class "locus-edit-panel"
                  :on-close #(reset! locus-to-edit nil)}
     [input/input {:eid  eid
                   :attr ::object/display-name
                   :label "Display Name"}]
     [:h2 "Scales"]
     (into [:ul.scale-controllers]
           (map (fn [{:keys [::object/id
                             ::object/display-name
                             ::scale-controller/address
                             ::scale/_device]}]
                  ^{:key (str id)}
                  [:li.scale-controller display-name
                   (into [:ul.scales]
                         (map (fn [{:keys [::object/display-name]}]
                                [:li.scale display-name]))
                         _device)]))
           @(re-posh/subscribe [::scales]))]))

(rf/reg-fx
 ::edit
 (fn [eid]
   (reset! locus-to-edit eid)))
