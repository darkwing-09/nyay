package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.models.*
import com.nyayasetu.data.repository.LawyerRepository
import com.nyayasetu.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LawyerViewModel @Inject constructor(
    private val repository: LawyerRepository
) : ViewModel() {

    private val _lawyerListState = MutableStateFlow<Resource<List<Lawyer>>>(Resource.Idle())
    val lawyerListState: StateFlow<Resource<List<Lawyer>>> = _lawyerListState.asStateFlow()

    private val _profileState = MutableStateFlow<Resource<LawyerProfile>>(Resource.Idle())
    val profileState: StateFlow<Resource<LawyerProfile>> = _profileState.asStateFlow()

    private val _followState = MutableStateFlow<Resource<GenericLawyerResponse>>(Resource.Idle())
    val followState: StateFlow<Resource<GenericLawyerResponse>> = _followState.asStateFlow()

    private val _feedState = MutableStateFlow<Resource<List<FeedPost>>>(Resource.Idle())
    val feedState: StateFlow<Resource<List<FeedPost>>> = _feedState.asStateFlow()

    fun fetchLawyers() {
        if (_lawyerListState.value is Resource.Loading) return
        viewModelScope.launch {
            repository.getLawyers().collect { _lawyerListState.value = it }
        }
    }

    fun fetchLawyerProfile(handle: String) {
        if (_profileState.value is Resource.Loading) return
        viewModelScope.launch {
            repository.getLawyerProfile(handle).collect { _profileState.value = it }
        }
    }

    fun followLawyer(handle: String) {
        if (_followState.value is Resource.Loading) return
        viewModelScope.launch {
            repository.followLawyer(handle).collect { 
                _followState.value = it 
                // Refresh profile after following
                if (it is Resource.Success) {
                    fetchLawyerProfile(handle)
                }
            }
        }
    }

    fun fetchFeed() {
        if (_feedState.value is Resource.Loading) return
        viewModelScope.launch {
            repository.getFeed().collect { _feedState.value = it }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            repository.likePost(postId).collect { result ->
                if (result is Resource.Success) {
                    val currentFeed = (_feedState.value as? Resource.Success)?.data ?: return@collect
                    val updatedList = currentFeed.map { post ->
                        if (post.post_id == postId) post.copy(is_liked = true, likes_count = post.likes_count + 1) else post
                    }
                    _feedState.value = Resource.Success(updatedList)
                }
            }
        }
    }

    fun resetFollowState() {
        _followState.value = Resource.Idle()
    }
}
