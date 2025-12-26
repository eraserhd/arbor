(ns cadro.ui.hiccup
  (:require
   [reagent.core :as r]))

(defn normalize1
  "Ensures hiccup is in the form [head props & rest], even if props is empty.

  Not recursive."
  [content]
  (let [[tag props & more] content]
    (cond
     (map? props)                   (into [tag props] more)
     (and (nil? props) (nil? more)) [tag {}]
     :else                          (into [tag {} props] more))))

(defn merge-props
  "Merge props into a hiccup form's existing props (which could be omitted)."
  [content props]
  (let [[tag existing-props & body] (normalize1 content)]
    (into [tag (r/merge-props existing-props props)]
          body)))
