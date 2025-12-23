(ns cadro.db
  (:require
   [datascript.core :as d]
   [re-posh.core :as re-posh]))

(def conn (d/create-conn))
(re-posh/connect! conn)
