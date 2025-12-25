(ns cadro.ui.panel)

(def close-icon [:i.fa-solid.fa-xmark])

(defn panel
  [{:keys [title on-close class]} & content]
  [:div.floating-card {:class class}
   [:div.header
    [:h1 title]
    [:button.close
     {:on-click #(on-close)}
     close-icon]]
   (into [:form] content)])
