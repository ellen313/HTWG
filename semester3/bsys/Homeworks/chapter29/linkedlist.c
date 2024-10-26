#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <sys/time.h>

//node structure
typedef struct __node_t
{
    int key;
    struct __node_t *next;
} node_t;

//list structure (one per list)
typedef struct __list_t
{
    node_t *head;
    pthread_mutex_t lock;
} list_t;

void List_Init(list_t *L)
{
    L->head = NULL;
    pthread_mutex_init(&L->lock, NULL);
}

void List_Insert(list_t *L, int key)
{
    node_t *new = malloc(sizeof(node_t));
    if (new == NULL)
    {
        perror("malloc");
        return;
    }
    new->key = key;

    //nur kritische sektion gelockt
    pthread_mutex_lock(&L->lock);
    new->next = L->head;
    L->head = new;
    pthread_mutex_unlock(&L->lock);
}

int List_Lookup(list_t *L, int key)
{
    int rv = -1;
    pthread_mutex_lock(&L->lock);
    node_t *curr = L->head;
    while (curr)
    {
        if (curr->key == key)
        {
            rv = 0;
            break;
        }
        curr = curr->next;
    }
    pthread_mutex_unlock(&L->lock);
    return rv; //erfolg und misserfolg
}

void List_Free(list_t *L) {
    pthread_mutex_lock(&L->lock);
    node_t *curr = L->head;
    while (curr) {
        node_t *next = curr->next;
        free(curr);
        curr = next;
    }
    pthread_mutex_unlock(&L->lock);
}

void List_Print(list_t *L) {
    pthread_mutex_lock(&L->lock);
    node_t *curr = L->head;
    while (curr) {
        printf("%d ", curr->key);
        curr = curr->next;
    }
    pthread_mutex_unlock(&L->lock);
    printf("\n");
}

void *thread_function(void *arg) {
    list_t *list = (list_t *)arg;
    for (int i = 0; i < 1000; i++) {
        List_Lookup(list, i);
    }
    return NULL;
}

// Main function
int main()
{
    list_t list;
    List_Init(&list);

    //ein paar Elemente in die Liste einfügen
    for (int i = 0; i < 10000; i++) {
        List_Insert(&list, i);
    }

    int num_threads = 4;
    pthread_t threads[num_threads];

    struct timeval start, end; //startzeit bekommen
    gettimeofday(&start, NULL);

    // Threads erstellen
    for (int i = 0; i < num_threads; i++) {
        pthread_create(&threads[i], NULL, thread_function, &list);
    }

    // auf alle Threads warten bis sie abgeschlossen sind
    for (int i = 0; i < num_threads; i++) {
        pthread_join(threads[i], NULL);
    }

    gettimeofday(&end, NULL); //endzeit bekommen

    //verstrichene Zeit in mikrosekunden berechnen
    long seconds = end.tv_sec - start.tv_sec;
    long microseconds = end.tv_usec - start.tv_usec;
    long elapsed = seconds * 1000000 + microseconds; 

    printf("verstrichene Zeit: %ld microseconds\n", elapsed);

    List_Free(&list);

    return 0;
}