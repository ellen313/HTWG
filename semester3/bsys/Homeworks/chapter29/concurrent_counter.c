#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

typedef struct __counter_t
{
    int value;
    pthread_mutex_t lock;
}counter_t;

void init(counter_t *c)
{
    c->value = 0;
    pthread_mutex_init(&c->lock, NULL);
}

void* increment(void *arg)
{
    counter_t *c = (counter_t *) arg;
    pthread_mutex_lock(&c->lock);
    for (int i = 0; i < 1000000; i++)
    {
        c->value++;
    }
    pthread_mutex_unlock(&c->lock);
    return NULL;
}

int get(counter_t *c) {
    pthread_mutex_lock(&c->lock);
    int val = c->value;
    pthread_mutex_unlock(&c->lock);
    return val;
}

int main()
{
    counter_t counter;
    init(&counter); //init fkt aufrufen um zähler auf 0 zu setzen und mutex zu initialisieren

    int num_threads = 4;
    pthread_t threads[num_threads]; //thread handler für jeden thread

    struct timeval start,end;
    gettimeofday(&start, NULL); //startzeit bekommen

    for (int i = 0; i < num_threads; i++)
    {
        pthread_create(&threads[i], NULL, increment, &counter); //neuen thread erstellen
    }

    for (int i = 0; i < num_threads; i++)
    {
        pthread_join(threads[i], NULL); //aufrufer blockieren bis thread beendet ist
    }

    gettimeofday(&end, NULL); //endzeit bekommen

    long seconds = end.tv_sec - start.tv_sec;
    long microseconds = end.tv_usec - start.tv_usec;
    long elapsed = seconds * 1000000 + microseconds;

    printf("Finaler Zählwert: %d\n", get(&counter)); //aktuellen wert ausgeben
    printf("verstrichene Zeit: %ld mikrosekunden\n", elapsed);

    return 0;
}