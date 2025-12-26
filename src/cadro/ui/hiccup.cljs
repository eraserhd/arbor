(ns cadro.ui.hiccup
  "Utilities for working with hiccup forms."
  (:require
   [reagent.core :as r]))

(defn normalize1
  "Ensures hiccup is in the form [head props & rest], even if props is empty.

  Not recursive."
  [form]
  (let [[tag props & more] form]
    (cond
     (map? props)                   (into [tag props] more)
     (and (nil? props) (nil? more)) [tag {}]
     :else                          (into [tag {} props] more))))

(defn merge-props
  "Merge props into a hiccup form's existing props (which could be omitted)."
  [form props]
  (let [[tag existing-props & body] (normalize1 form)]
    (into [tag (r/merge-props existing-props props)]
          body)))
