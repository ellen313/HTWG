#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <unistd.h>

#define NUMCPUS 4 //Beispielwert

typedef struct __counter_t
{
    int global; //global count
    pthread_mutex_t glock; // global lock
    int local[NUMCPUS]; // local count per CPU
    pthread_mutex_t llock[NUMCPUS]; // local count per locks
    int threshold; // update frequency (local to global transfer)
} counter_t;

void init(counter_t *c, int threshold)
{
    c->threshold = threshold; //threshold (Schwelle) setzen
    pthread_mutex_init(&c->glock, NULL); //glock initialisieren

    for (int i = 0; i < NUMCPUS; i++)
    {
        c->local[i] = 0; //lokalen Zähler auf 0 setzen
        pthread_mutex_init(&c->llock[i], NULL);
    }
}

void update(counter_t *c, int threadID, int amt)
{
    pthread_mutex_lock(&c->llock[threadID]); // lokalen lock für aktuellen Thread sichern
    c->local[threadID] += amt; //lokalen Zähler erhöhen
    if (c->local[threadID] >= c->threshold) //prüft ob lokaler Zähler Schwelle überschritten hat
    {
        pthread_mutex_lock(&c->glock); // globalen lock sichern, um globalen Zähler zu aktualisieren
        c->global += c->local[threadID]; // wert des lokalen Zählers auf global übertragen
        pthread_mutex_unlock(&c->glock); //global lock freigeben
        c->local[threadID] = 0; //lokalen Zähler auf 0 zurücksetzen
    }
    pthread_mutex_unlock(&c->llock[threadID]); //lokalen lock freigeben
}

int get(counter_t *c)
{
    pthread_mutex_lock(&c->glock); //globalen lock sichern um globalen Zähler sicher abzurufen
    int val = c->global; //aktuellen wert des globalen Zählers speichern
    pthread_mutex_unlock(&c->glock); //globalen lock freigeben
    return val; //globalen Zählwert zurückgeben
}

void* thread_func(void *arg) //von jedem thread ausgeführt
{
    int id = *(int*)arg; //jeder thread erhält seine id
    counter_t counter;
    for (int i = 0; i < 1000000; i++) //und führt 1mio mal inkrementierungen auf dem counter durch
    {
        update(&counter, id, 1);
    }
    return NULL;
}

int main()
{
    counter_t counter;
    init(&counter, 10); //threshold auf 10 setzen, notwenigen mutex initialisieren

    pthread_t threads[NUMCPUS]; //ein thread handle für jede cpu
    int thread_ids[NUMCPUS]; //ids werden den einzelnen threads übergeben

    for (int i = 0; i < NUMCPUS; i++)
    {
        thread_ids[i] = i;
        pthread_create(&threads[i], NULL, thread_func, &thread_ids[i]); //erstellt und startet die threads, jeder erhält funktion und id
    }

    for (int i = 0; i < NUMCPUS; i++)
    {
        pthread_join(threads[i], NULL); //wartet dass thread Ausführung beendet (Programm fährt erst fort wenn alle fertig)
    }

    printf("Finaler Zählwert: %d\n", get(&counter)); //ruft aktuellen wert des counters ab, indem lock verwendet wird

    return 0;
}