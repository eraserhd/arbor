(ns cadro.ui.locus
  (:require
   [cadro.model.object :as object]
   [cadro.ui.input :as input]
   [cadro.ui.panel :as panel]
   [reagent.core :as ra]
   [re-frame.core :as rf]
   [re-posh.core :as re-posh]))

(def ^:private locus-to-edit (ra/atom nil))

(defn edit-panel []
  (fn []
    (when-let [eid @locus-to-edit]
      [panel/panel {:title "Edit Locus"
                    :class "locus-edit-panel"
                    :on-close #(reset! locus-to-edit nil)}
       [input/label eid ::object/display-name "Display Name"]
       [input/input eid ::object/display-name]])))

(rf/reg-fx
 ::edit
 (fn [eid]
   (reset! locus-to-edit eid)))
