#include "ulstack.h"

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define size 200

typedef struct {
    unsigned long *elementPtr; //Zeiger welcher auf das Daten-Array verweist
    unsigned int stackSize; //gesamtanzahl von Elementen auf dem Stack
    unsigned int allocSize; //gesamt allokierte Größe in Bytes (sizeof)
} ulstack;

void ulStackNew (ulstack * s)
{
    s->elementPtr = malloc(sizeof(unsigned long)*size);
    s->stackSize = 0;
    s->allocSize = size;
}

void ulStackDispose (ulstack * s)
{
    free(s->elementPtr);
    s->stackSize = 0;
    s->allocSize = 0;
}

void ulStackPush (ulstack * s, unsigned long value)
{
    if (s->stackSize == s->allocSize)
    {
        s->allocSize = s->allocSize *2;
        s->elementPtr = realloc(s->elementPtr, sizeof());
        //checking if realloc was successful 
        if (elementPtr == NULL)
        {
        fprintf(stderr, "allocation failed");
        return 1;
        }  
    }
    s->elementPtr[s->stackSize] = value;
    s->stackSize++;
}

unsigned long ulStackPop (ulstack * s)
{
    if (s->stackSize > 0) //if operation is possible
    {
        //take elements from stack
        unsigned long poppedValue = s->elementPtr[s->stackSize - 1];
        s->stackSize--;
        return poppedValue;
    } else 
    {
        //stack is empty
        assert(s->stackSize > 0);
        return 0;
    }
}

unsigned int ulStackSize (ulstack * s)
{
    return s->stackSize;
}

