(ns cadro.ui.panel)

(defn panel
  [{:keys [title on-close]} & content]
  [:div.floating-card.locus-edit-panel
   [:div.header
    [:h1 title]
    [:button.close
     {:on-click #(on-close)}
     [:i.fa-solid.fa-xmark]]]
   (into [:form] content)])
