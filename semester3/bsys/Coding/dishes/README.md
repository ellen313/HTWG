Unsere Aufgabe war es, einen gegebenen Code so umzuschreiben bzw. zu verbessern, dass jeder Prozess (dargestellt durch die 3 verschiedenen Tiere Hund, Katze, Maus) so lange wie möglich läuft (kein Tier verhungert) und sich nicht gegenseitig verhindern (fressen eines anderen Tieres).
Dies haben wir folgendermaßen gelöst bzw. umgesetzt:

In dem Scheduler wird jedes Tier durch einen eigenen Prozess dargestellt, was bedeutet, dass die gemeinsam genutzten Ressourcen 
(hier Futterschalen), synchronisiert werden müssen, damit es nicht zu Race Condtions kommt.
Hierfür haben wir den Lock `dish_lock` und die Condition Variable `feed_cond` eingebaut. 

Da ein Lock verwendet wird, um kritische Abschnitte im Programm zu synchronisieren verwenden wir hier `dish_lock`, um den kritischen Abschnitt im Code (in der main Methode `goto_dishes()`) zu schützen. Dies sorgt dafür, dass nur ein Thread zur gleichen Zeit Zugriff auf die gemeinsam genutzte Futterschale hat. In Zeile 109 definiert `with feed_cond` den kritischen Abschnitt. Es sorgt dafur, dass `dish_lock` beim Betreten eines Threads in den kritischen Abschnitt automatisch gesperrt und beim Verlassen wieder entsperrt wird.
Sprich, durch `with feed_cond` wird implizit der zugehörige Lock erworben und nach Abschluss des kritischen Abschnitts (nachdem die Tiere gefüttert wurden und `notify_all()` aufgerufen wurde) wird der Lock automatisch wieder freigegeben.

Wir haben eine CV angewendet, da sie es Threads generell ermöglicht, auf Änderungen eines gemeinsamen Zustands zu warten und informiert zu werden, wenn dieser Zustand sich ändert.
Die Condition Variable `feed_cond` wird mit dem Lock `dish_lock` initialisiert, was sicher stellt, dass die Operationen, die auf `feed_cond` angewendet werden (wie `notify_all()`), atomar innerhalb des kritischen Abschnitts des `dish_lock` ausgeführt werden. Mit der CV können die Zustandsänderungen (das Futter ist bereit) gespeichert werden, auf welche die Threads dementsprechend reagieren können. 

In der main-Funktion des Schedulers verwenden wir die`notify_all()`- Methode mit der Threads andere darüber informieren können, dass eine bestimmte Bedingnung erfüllt ist bzw. dass eine Änderung eingetreten ist: Wenn ein Thread seine Aufgaben im kritischen Abschnitt beendet hat, wird mit diesem Funktionsaufruf allen anderen wartenenden Threads signalisiert, dass sich der Zustand geändert hat und sie ihre Arbeit (naja das Fressen) fortsetzen können.
