<template>
  <div class="message-page">
    <AppHeader />
    <div class="container mt-20">
      <el-card class="chat-container">
        <el-row :gutter="0" style="height: 600px">
          <el-col :span="6" class="contact-sidebar">
            <div class="sidebar-header">
              <el-input v-model="searchContact" placeholder="搜索联系人" size="small" clearable />
            </div>
            <div class="contact-list">
              <div
                v-for="contact in contacts"
                :key="contact.id"
                class="contact-item"
                :class="{ active: currentContact?.id === contact.id }"
                @click="selectContact(contact)"
              >
                <el-avatar :size="40">{{ contact.name[0] }}</el-avatar>
                <div class="contact-info">
                  <div class="contact-name">{{ contact.name }}</div>
                  <div class="contact-last-msg text-muted">{{ contact.lastMessage }}</div>
                </div>
                <el-badge v-if="contact.unread" :value="contact.unread" class="unread-badge" />
              </div>
            </div>
          </el-col>

          <el-col :span="18" class="chat-main">
            <template v-if="currentContact">
              <div class="chat-header">
                <span class="chat-title">{{ currentContact.name }}</span>
              </div>
              <div class="chat-messages" ref="messagesRef">
                <div
                  v-for="msg in messages"
                  :key="msg.id"
                  class="message-item"
                  :class="msg.isSelf ? 'message-self' : 'message-other'"
                >
                  <el-avatar :size="36" class="msg-avatar">{{ msg.userName[0] }}</el-avatar>
                  <div class="message-bubble">
                    <div class="msg-content">{{ msg.content }}</div>
                    <div class="msg-time text-muted">{{ msg.time }}</div>
                  </div>
                </div>
              </div>
              <div class="chat-input">
                <el-input
                  v-model="inputMsg"
                  type="textarea"
                  :rows="3"
                  placeholder="输入消息..."
                  @keydown.ctrl.enter="sendMessage"
                />
                <div class="input-actions flex-between mt-10">
                  <span class="text-muted">Ctrl + Enter 发送</span>
                  <el-button type="primary" @click="sendMessage">发送</el-button>
                </div>
              </div>
            </template>
            <el-empty v-else description="请选择一个联系人开始聊天" style="height: 100%; display: flex; align-items: center" />
          </el-col>
        </el-row>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const searchContact = ref('')
const currentContact = ref(null)
const inputMsg = ref('')
const messagesRef = ref(null)

const contacts = ref([
  { id: 1, name: '张明', lastMessage: '好的，明天交付', unread: 2 },
  { id: 2, name: '李华', lastMessage: '需求文档已发', unread: 0 },
  { id: 3, name: '王强', lastMessage: '尾款已收到', unread: 1 },
  { id: 4, name: '赵敏', lastMessage: '方案已确认', unread: 0 }
])

const messages = ref([
  { id: 1, userName: '张明', content: '你好，项目进展如何了？', time: '10:30', isSelf: false },
  { id: 2, userName: '我', content: '已完成80%，预计明天可以交付', time: '10:32', isSelf: true },
  { id: 3, userName: '张明', content: '好的，明天交付', time: '10:33', isSelf: false }
])

function selectContact(contact) {
  currentContact.value = contact
  contact.unread = 0
}

function sendMessage() {
  if (!inputMsg.value.trim()) return
  messages.value.push({
    id: Date.now(),
    userName: '我',
    content: inputMsg.value,
    time: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
    isSelf: true
  })
  inputMsg.value = ''
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}
</script>

<style scoped lang="scss">
.chat-container {
  overflow: hidden;
}

.contact-sidebar {
  border-right: 1px solid #ebeef5;
  height: 600px;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 12px;
  border-bottom: 1px solid #ebeef5;
}

.contact-list {
  flex: 1;
  overflow-y: auto;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;

  &:hover {
    background: #f5f7fa;
  }

  &.active {
    background: #ecf5ff;
  }
}

.contact-info {
  flex: 1;
  margin-left: 10px;
  overflow: hidden;
}

.contact-name {
  font-weight: bold;
  font-size: 14px;
}

.contact-last-msg {
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-badge {
  margin-left: 8px;
}

.chat-main {
  height: 600px;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 12px 16px;
  border-bottom: 1px solid #ebeef5;

  .chat-title {
    font-weight: bold;
    font-size: 16px;
  }
}

.chat-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.message-item {
  display: flex;
  margin-bottom: 16px;
  gap: 10px;
}

.message-self {
  flex-direction: row-reverse;

  .message-bubble {
    background: #ecf5ff;
  }
}

.message-bubble {
  max-width: 60%;
  background: #f0f0f0;
  padding: 10px 14px;
  border-radius: 8px;
}

.msg-content {
  line-height: 1.5;
}

.msg-time {
  font-size: 11px;
  margin-top: 4px;
  text-align: right;
}

.chat-input {
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
}

.text-muted {
  color: #909399;
}
</style>
