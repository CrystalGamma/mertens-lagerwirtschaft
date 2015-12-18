# Observer Pattern
Das Observer Pattern ist ein Design Pattern, dass für ereignisgesteuerte Anwendungen nützlich ist.
Es dient dazu, den oder die Ereignisempfänger von der Ereignisquelle zu entkoppeln.

In diesem Projekt wird das Observer Pattern benutzt, um das Model von View und Controller und View vom Controller zu entkoppeln.
Somit hängt der Controller vom Model ab, da er an diesem Änderungen vornimmt und vom View, da er diesen erzeugt.
Das Model ist von keinem der beiden anderen Einheiten abhängig und ist somit am leichtesten wiederverwendbar änderbar.

Jede View ist ein Observer des Models, um Änderungen am Model durch neurendern darstellen zu können.
Manche Views stellen außerdem ein Observable zur Verfügung, um Transaktionen an den Controller weiterleiten zu können.

# Strategy
Das Strategy-Pattern dient dazu, Nutzern einer Klasse zu erlauben, das Verhalten zu beeinflussen, ohne die Implementation zu kennen.
Die veränderbaren Aspekte werden als Interfaces definiert, von denen Implementationen, je nach Bedarf des Nutzers, der Instanz zur Verfügung gestellt werden.

Ein wichtiger Einsatz des Strategy Pattern ist im Lieferungsdialog, der je nach Situation eine Zu- oder Auslieferung erstellen soll.
Hier sind die austauschbaren Algorithmen die Auswahl des Vorzeichens für den Buchungswert und die Bestimmung, wieviel noch in/aus einem Lager geliefert werden kann.