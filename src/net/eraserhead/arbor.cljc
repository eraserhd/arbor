(ns net.eraserhead.arbor
  (:require
   [net.eraserhead.arbor.loci :as loci]))

(def initial-state
  {::loci/db loci/empty-db})
