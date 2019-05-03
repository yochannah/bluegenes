(ns bluegenes.subs.auth
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::auth
 (fn [db]
   (get-in db [:auth])))

(reg-sub
 ::identity
 (fn [db]
   (get-in db [:auth :identity])))

(reg-sub
 ::authenticated?
 :<- [::identity]
 (fn [identity]
   (some? (not-empty identity))))

(defn is-https? [url]
  "Checks if a given URL is https."
  (clojure.string/starts-with? url "https://"))

(defn is-on-localhost? []
  "Checks if the app is hosted locally or not"
  (clojure.string/starts-with?  (.-host (.-location js/window)) "localhost"))

(reg-sub
 ::allow-logon?
 (fn [db]
   (let [current-mine (get db :current-mine)
         mine-url (get-in db [:mines current-mine :service :root])
         is-https? (is-https? mine-url)
         is-on-localhost? (is-on-localhost?)]
     (or is-https? is-on-localhost?))))
