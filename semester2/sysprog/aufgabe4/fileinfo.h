/*
 * fileinfo.h
 *
 * Author Ellen Peppmueller
 * Erstellt am: 01.12.2023
 */

#ifndef FILEINFO_H
#define FILEINFO_H

#include <stddef.h> //size_t
#include <limits.h>
#include <stdint.h>

enum filetype
{
    filetype_regular,
    filetype_directory,
    filetype_other
};

struct fileinfo
{
    struct fileinfo *next;
    char name[NAME_MAX + 1];
    enum filetype type;
    union
    {
        size_t size; // for regular files
        struct fileinfo *list; // for directories
    };
    

};

typedef struct fileinfo fileinfo; //aliasname 

fileinfo* fileinfo_create(const char *path);

void fileinfo_print(fileinfo* file);

void fileinfo_destroy(fileinfo* file);

#endif //FILEINFO_H