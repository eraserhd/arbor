(ns cadro.ui.input
  (:require
   [re-posh.core :as re-posh]
   [re-frame.core :as rf]))

(re-posh/reg-sub
 ::value
 (fn [_ [_ eid attr]]
   {:type      :query
    :query     '[:find ?value
                 :in $ ?eid ?attr
                 :where [?eid ?attr ?value]]
    :variables [eid attr]}))

(re-posh/reg-event-ds
 ::set-value
 (fn [_ [_ eid attr value]]
   [[:db/add eid attr value]]))

(defn control-name
  "Derive the name for a form control from its database attribute."
  [attr]
  (str (namespace attr) "__" (name attr)))

(defn input
  "Input element for an object attribute in the datastore."
  [eid attr]
  (let [value (re-posh/subscribe [::value eid attr])]
    (fn []
      [:input {:name (control-name attr)
               :default-value @value
               :on-blur (fn [e]
                          (let [value (.. e -target -value)]
                            (rf/dispatch [::set-value eid attr value])))}])))
