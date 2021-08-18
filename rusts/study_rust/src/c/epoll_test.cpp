//
// Created by jarvmuqiliu on 2021/8/17.
//
#include <stdio.h>
#include <unistd.h>
#include <sys/epoll.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>

#define MAX_EVENTS 1

int main(void) {
    int epfd;
    epfd = epoll_create(100); /* 创建epoll实例，预计监听100个fd */
    if (epfd < 0) {
        perror("epoll_create");
    }

    struct epoll_event *events;
    int nr_events, i;
    events = malloc(sizeof(struct epoll_event) * MAX_EVENTS);
    if (!events) {
        perror("malloc");
        return 1;
    }

    /* 打开一个普通文本文件 */
    int target_fd = open("./11.txt", O_RDONLY);
    printf("target_fd %d\n", target_fd);
    int target_listen_type = EPOLLIN;
    for (i = 0; i < 1; i++) {
        int ret;
        events[i].data.fd = target_fd; /* epoll调用返回后，返回给应用进程的fd号 */
        events[i].events = target_listen_type; /* 需要监听的事件类型 */
        ret = epoll_ctl(epfd, EPOLL_CTL_ADD, target_fd, &events[i]); /* 注册fd到epoll实例上 */
        if (ret) {
            printf("ret %d, errno %d\n", ret, errno);
            perror("epoll_ctl");
        }
    }

    /* 应用进程阻塞在epoll上，超时时长置为-1表示一直等到有目标事件才会返回 */
    nr_events = epoll_wait(epfd, events, MAX_EVENTS, -1);
    if (nr_events < 0) {
        perror("epoll_wait");
        free(events);
        return 1;
    }
    for (i = 0; i < nr_events; i++) {
        /* 打印出处于就绪状态的fd及其事件 */
        printf("event=%d on fd=%d\n", events[i].events, events[i].data.fd);
    }
    free(events);
    close(epfd);
    return 0;
}

