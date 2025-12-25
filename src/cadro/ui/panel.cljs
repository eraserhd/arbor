(ns cadro.ui.panel
  (:require
   [reagent.core :as r]
   ["hammerjs" :as Hammer]))

(def close-icon [:i.fa-solid.fa-xmark])

(defn panel
  [{:keys [title on-close class]} & content]
  (let [on-keydown (fn [e]
                     (when (= "Escape" (.-key e))
                       (on-close)))
        panel-ref  (atom nil)]
    (r/create-class
     {:reagent-render
      (fn []
       [:div.floating-card {:ref   #(reset! panel-ref %)
                            :class class}
        [:div.header
         [:h1 title]
         [:button.close
          {:on-click on-close}
          close-icon]]
        (into [:form] content)])

      :component-did-mount
      (fn [this]
        (when-let [panel @panel-ref]
          (let [hammer (Hammer/Manager. panel)]
            (r/set-state this {:hammer hammer})
            (.add hammer (Hammer/Swipe. #js {:direction Hammer/DIRECTION_VERTICAL}))
            (.on hammer "swipedown" on-close)))
        (.addEventListener js/document "keydown" on-keydown))

      :component-will-unmount
      (fn [this]
        (.removeEventListener js/document "keydown" on-keydown)
        (when-let [hammer (:hammer (r/state this))]
          (.destroy hammer)))})))
