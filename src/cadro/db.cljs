(ns cadro.db
  (:require
   [cadro.model.object :as object]
   [datascript.core :as d]
   [re-posh.core :as re-posh]))

(def conn (d/create-conn object/schema))
(re-posh/connect! conn)
