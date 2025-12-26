(ns cadro.ui.keys
  (:refer-clojure :exclude [keys])
  (:require
   [reagent.core :as r]))

(defn keys
  "Wraps content, and globally handles keys while visible."
  [{:keys [on-keys]} content]
  (let [on-keys-ref (atom on-keys)
        on-keydown  (fn [e]
                      (when-let [handler (get @on-keys-ref (.-key e))]
                        (handler)))]
    (r/create-class
     {:reagent-render
      (fn [{:keys [on-keys]} content]
        (reset! on-keys-ref on-keys)
        content)

      :component-did-mount
      (fn [this]
        (.addEventListener js/document "keydown" on-keydown))

      :component-will-unmount
      (fn [this]
        (.removeEventListener js/document "keydown" on-keydown))})))
