import { Image, Pressable, Text, View } from 'react-native';
import { Product } from '../api';
import { colors, money } from '../theme';

const fallbackImage = 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=900';
export function ProductCard({ item, saved, onAdd, onSave, onSelect }:
    { item: Product; saved: boolean; onAdd: (id: string) => void; onSave: (id: string) => void; onSelect: (item: Product) => void }) {
    return <View style={{ width: '47%', backgroundColor: colors.white, paddingBottom: 12, marginBottom: 4, position: 'relative' }}>
        <Pressable onPress={() => onSelect(item)}>
            <Image source={{ uri: item.imageUrls?.[0] || fallbackImage }} style={{ width: '100%', height: 150, backgroundColor: '#e6e6df' }} />
            <Text style={{ color: colors.gold, fontSize: 9, fontWeight: '800', letterSpacing: 1, marginHorizontal: 11, marginTop: 11 }}>{item.categoryName || 'ESSENTIAL'}</Text>
            <Text style={{ fontSize: 15, fontWeight: '700', color: colors.ink, marginHorizontal: 11, marginTop: 4 }} numberOfLines={1}>{item.name}</Text>
            <Text style={{ fontSize: 14, fontWeight: '700', color: '#405742', marginHorizontal: 11, marginTop: 5 }}>{money(item.price)}</Text>
        </Pressable>
        <Pressable style={{ position: 'absolute', top: 8, right: 8, backgroundColor: colors.white, width: 31, height: 31, borderRadius: 16, alignItems: 'center', justifyContent: 'center' }} onPress={() => onSave(item.id)}>
            <Text style={{ fontSize: 19, color: '#9b664d' }}>{saved ? '♥' : '♡'}</Text>
            </Pressable>
        <Pressable style={{ position: 'absolute', bottom: 11, right: 10, width: 27, height: 27, borderRadius: 14, backgroundColor: colors.sage, alignItems: 'center', justifyContent: 'center' }} onPress={() => onAdd(item.id)}><Text style={{ fontSize: 22, color: '#2c5033' }}>+</Text></Pressable>
    </View>;
}
