<template>
    <div>
        <h1 class="title">Users</h1>
        <h1 class="email">{{userName}}</h1>
        <section class="userapp">
            <div v-if="loading">
                <h1 class="loading">Loading...</h1>
            </div>
            <div v-else>
                <header class="header">
                    <input class="new-user"
                           autofocus autocomplete="off"
                           :placeholder="this.inputPlaceholder"
                           v-model="newUser"
                           @keyup.enter="addUser">
                </header>
                <section class="main" v-show="users.length" v-cloak>
                    <input class="toggle-all" type="checkbox" v-model="allDone">
                    <ul class="user-list">
                        <li v-for="user in filteredUsers"
                            class="user"
                            :key="user.id"
                            :class="{ active: user.active, editing: user == editedUser }">
                            <div class="view">
                                <input class="toggle" type="checkbox" v-model="user.active" @change="makeActive(user)">
                                <label @dblclick="editUser(user)">{{ user.name }}</label>
                                <button class="destroy" @click="removeUser(user)"></button>
                            </div>
                            <input class="edit" type="text"
                                   v-model="user.name"
                                   v-user-focus="user == editedUser"
                                   @blur="doneEdit(user)"
                                   @keyup.enter="doneEdit(user)"
                                   @keyup.esc="cancelEdit(user)">
                        </li>
                    </ul>
                </section>
                <footer class="footer" v-show="users.length" v-cloak>
          <span class="user-count">
            <strong>{{ remaining }}</strong> {{ remaining | pluralize }} left
          </span>
                    <ul class="filters">
                        <li><a href="#/all" @click="setVisibility('all')" :class="{ selected: visibility == 'all' }">All</a></li>
                        <li><a href="#/active" @click="setVisibility('active')" :class="{ selected: visibility == 'active' }">Active</a></li>
                        <li><a href="#/completed" @click="setVisibility('active')" :class="{ selected: visibility == 'active' }">Completed</a></li>
                    </ul>
                    <button class="clear-completed" @click="removeCompleted" v-show="users.length > remaining">
                        Make all inactive
                    </button>
                </footer>
            </div>
        </section>
        <div v-if="error" class="error" @click="handleErrorClick">
            ERROR: {{this.error}}
        </div>
    </div>
</template>

<script>

    import api from '../api.js';

    // visibility filters
    let filters = {
        all: function (users) {
            return users
        },
        active: function (users) {
            return users.filter(function (user) {
                return !user.active
            })
        }
    }

    // app Vue instance
    const Users = {
        name: 'Users',
        props: {
            activeUser: Object
        },

        // app initial state
        data: function() {
            return {
                users: [],
                newUser: '',
                editedUser: null,
                visibility: 'all',
                loading: true,
                error: null,
            }
        },

        mounted() {
            api.getAll()
                .then(response => {
                    this.$log.debug("Data loaded: ", response.data)
                    this.users = response.data
                })
                .catch(error => {
                    this.$log.debug(error)
                    this.error = "Failed to load users"
                })
                .finally(() => this.loading = false)
        },

        computed: {
            filteredUsers() {
                return filters[this.visibility](this.users)
            },
            remaining() {
                return filters.active(this.users).length
            },
            allDone: {
                get() {
                    return this.remaining === 0
                },
                set(value) {
                    this.users.forEach(function (todo) {
                        todo.active = value
                    })
                }
            },
            userName() {
                return this.name
            },
            inputPlaceholder() {
                return "Input a name here!"
            }
        },

        filters: {
            pluralize: function (n) {
                return n === 1 ? 'item' : 'items'
            }
        },

        // methods that implement data logic.
        // note there's no DOM manipulation here at all.
        methods: {

            addUser: function () {
                var value = this.newUser && this.newUser.trim();
                if (!value) {
                    return
                }

                api.createNew(value, false).then( (response) => {
                    this.$log.debug("New user created:", response);
                    this.users.push({
                        id: response.data.id,
                        name: value,
                        active: false
                    })
                }).catch((error) => {
                    this.$log.debug(error);
                    this.error = "Failed to add user"
                });

                this.newUser = ''
            },

            setVisibility: function(vis) {
                this.visibility = vis
            },

            makeUserActive (user) {
                api.updateForId(user.id, user.name, user.active).then((response) => {
                    this.$log.info("User updated:", response.data);
                }).catch((error) => {
                    this.$log.debug(error);
                    user.completed = !user.active;
                    this.error = "Failed to update todo"
                });
            },
            removeUser: function (user) {
                this.$log.info(user);
                api.removeForId(user.id).then(() => {
                    this.$log.debug("User removed:", user);
                    this.users.splice(this.users.indexOf(user), 1);
                }).catch((error) => {
                    this.$log.debug(error);
                    this.error = "Failed to remove user";
                });
            },

            editUser: function (user) {
                this.beforeEditCache = user.name
                this.editedUser = user
            },

            doneEdit: function (user) {
                if (!this.editedUser) {
                    return
                }
                this.$log.info("User updated:", user);
                api.updateForId(user.id, user.name.trim(), user.active).then((response) => {
                    this.$log.info("User updated:", response.data);
                    this.editedUser = null
                    user.name = user.name.trim()
                }).catch((error) => {
                    this.$log.debug(error)
                    this.cancelEdit(user)
                    this.error = "Failed to update user"
                });

                if (!user.name) {
                    this.removeUser(user)
                }
            },

            cancelEdit: function (user) {
                this.editedUser = null
                user.name = this.beforeEditCache
            },

            removeCompleted: function () {
                this.users = filters.active(this.users)
            },

            handleErrorClick: function () {
                this.error = null;
            },
        },

        directives: {
            'user-focus': function (el, binding) {
                if (binding.value) {
                    el.focus()
                }
            }
        }
    }

    export default Users
</script>

<style>
    [v-cloak] { display: none; }
</style>