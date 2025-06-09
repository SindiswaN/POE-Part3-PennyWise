package vcmsa.projects.sindiswasinazo.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import vcmsa.projects.sindiswasinazo.data.models.*

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // Coroutine-compatible login function
    suspend fun login(email: String, password: String): Boolean = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user != null
    } catch (e: Exception) {
        false
    }


    // --- CATEGORY ---

    fun addCategory(category: Category, onResult: (Boolean) -> Unit) {
        val uid = getCurrentUserId()
        if (uid == null) {
            Log.e("AddCategory", "User not logged in")
            onResult(false)
            return
        }

        val categoryId = database.child("categories").child(uid).push().key
        if (categoryId == null) {
            Log.e("AddCategory", "Could not generate categoryId")
            onResult(false)
            return
        }

        val newCategory = category.copy(id = categoryId)

        database.child("categories").child(uid).child(categoryId)
            .setValue(newCategory)
            .addOnSuccessListener {
                Log.d("AddCategory", "Category added successfully")
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e("AddCategory", "Failed to add category", exception)
                onResult(false)
            }
    }
    fun getCategories(onData: (List<Category>) -> Unit) {
        val uid = getCurrentUserId() ?: return
        database.child("categories").child(uid)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(Category::class.java) }
                    onData(list)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // --- ENTRY ---

    fun addEntry(entry: Entry, onResult: (Boolean) -> Unit) {
        val uid = getCurrentUserId() ?: return onResult(false)

        // Count existing entries to create custom ID
        database.child("entries").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nextIndex = snapshot.childrenCount + 1
                    val entryId = "Entry $nextIndex"
                    val newEntry = entry.copy(id = entryId)

                    database.child("entries").child(uid).child(entryId)
                        .setValue(newEntry)
                        .addOnCompleteListener { onResult(it.isSuccessful) }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseRepo", "Counting entries failed", error.toException())
                    onResult(false)
                }
            })
    }

    // Get all entries
    fun getEntries(onData: (List<Entry>) -> Unit) {
        val uid = getCurrentUserId() ?: return
        database.child("entries").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(Entry::class.java) }
                    onData(list)
                }
                override fun onCancelled(error: DatabaseError) { /* ignore */ }
            })
    }

    fun getCategoriesFlow(): Flow<List<Category>> = callbackFlow {
        val uid = getCurrentUserId() ?: run {
            close()
            return@callbackFlow
        }
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                for (item in snapshot.children) {
                    val category = item.getValue(Category::class.java)
                    if (category != null) list.add(category)
                }
                trySend(list).isSuccess
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }


        database.child("categories").child(uid).addValueEventListener(listener)
        awaitClose {
            database.child("categories").child(uid).removeEventListener(listener)
        }
    }

    fun getEntriesFlow(): Flow<List<Entry>> = callbackFlow {
        val uid = getCurrentUserId() ?: run {
            close()
            return@callbackFlow
        }
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Entry>()
                for (item in snapshot.children) {
                    val entry = item.getValue(Entry::class.java)
                    if (entry != null) list.add(entry)
                }
                trySend(list).isSuccess
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.child("entries").child(uid).addValueEventListener(listener)
        awaitClose {
            database.child("entries").child(uid).removeEventListener(listener)
        }
    }


    fun getEntriesBetweenDates(start: Long, end: Long, onData: (List<Entry>) -> Unit) {
        val uid = getCurrentUserId() ?: return
        database.child("entries").child(uid)
            .orderByChild("date").startAt(start.toDouble()).endAt(end.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Entry>()
                    for (item in snapshot.children) {
                        val entry = item.getValue(Entry::class.java)
                        if (entry != null) list.add(entry)
                    }
                    onData(list)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // --- GOALS ---

    fun setGoal(goal: Goal, onResult: (Boolean) -> Unit) {
        val uid = getCurrentUserId() ?: return onResult(false)
        val key = goal.categoryId ?: "overall"
        database.child("goals").child(uid).child(key)
            .setValue(goal).addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun getGoals(onData: (Map<String, Goal>) -> Unit) {
        val uid = getCurrentUserId() ?: return
        database.child("goals").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val map = mutableMapOf<String, Goal>()
                    for (item in snapshot.children) {
                        val goal = item.getValue(Goal::class.java)
                        if (goal != null) {
                            val key = item.key ?: continue
                            map[key] = goal
                        }
                    }
                    onData(map)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    // --- BADGES ---

    fun addBadge(badge: Badge, onResult: (Boolean) -> Unit) {
        val uid = getCurrentUserId() ?: return onResult(false)
        val badgeId = database.child("badges").child(uid).push().key ?: return onResult(false)

        val newBadge = Badge(
            id = badgeId,
            name = badge.name,
            description = badge.description,
            imageUrl = badge.imageUrl,
            achieved = badge.achieved,
            dateAchieved = badge.dateAchieved
        )

        database.child("badges").child(uid).child(badgeId)
            .setValue(newBadge).addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun getBadges(onData: (List<Badge>) -> Unit) {
        val uid = getCurrentUserId() ?: return
        database.child("badges").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Badge>()
                    for (item in snapshot.children) {
                        val badge = item.getValue(Badge::class.java)
                        if (badge != null) list.add(badge)
                    }
                    onData(list)
                }

                override fun onCancelled(error: DatabaseError) {}


            })

    }



    fun getBadgesFlow(): Flow<List<Badge>> = callbackFlow {
        val uid = getCurrentUserId() ?: run {
            close()
            return@callbackFlow
        }
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Badge>()
                for (item in snapshot.children) {
                    val badge = item.getValue(Badge::class.java)
                    if (badge != null) list.add(badge)
                }
                trySend(list).isSuccess
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("badges").child(uid).addValueEventListener(listener)
        awaitClose {
            database.child("badges").child(uid).removeEventListener(listener)
        }
    }

    // --- USERS ---
    suspend fun register(email: String, password: String, username: String): Boolean {
        return try {
            // 1. Create user with Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: return false

            // 2. Prepare a User object with UID and username
            val user = User(
                uid = firebaseUser.uid,
                email = email,
                username = username
            )

            // 3. Save the user to Realtime Database under "users/uid"
            database.child("users").child(firebaseUser.uid).setValue(user).await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    fun addUser(user: User, onResult: (Boolean) -> Unit) {
        val uid = user.uid.ifEmpty { getCurrentUserId() ?: return onResult(false) }
        database.child("users").child(uid)
            .setValue(user).addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun getUser(uid: String, onData: (User?) -> Unit) {
        database.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    onData(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    onData(null)
                }

            })
    }
}

