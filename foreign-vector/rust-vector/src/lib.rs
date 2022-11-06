use std::ops::*;

#[derive(Debug, PartialEq, Clone, Copy, Default)]
#[repr(C)]
pub struct Vec3 {
    pub vector3: [f64; 3],
}

impl Vec3 {
    #[no_mangle]
    pub extern "C" fn empty_vec() -> Vec3 {
        Self::default()
    }

    #[no_mangle]
    pub extern "C" fn new_with_values(x: f64, y: f64, z: f64) -> Vec3 {
        Vec3 { vector3: [x, y, z] }
    }

    #[no_mangle]
    pub extern "C" fn fill_with_values(&mut self, x: f64, y: f64, z: f64) {
        self.vector3[0] = x;
        self.vector3[1] = y;
        self.vector3[2] = z;
    }

    #[no_mangle]
    pub extern "C" fn x(&self) -> f64 {
        self.vector3[0]
    }

    #[no_mangle]
    pub extern "C" fn y(&self) -> f64 {
        self.vector3[1]
    }

    #[no_mangle]
    pub extern "C" fn z(&self) -> f64 {
        self.vector3[2]
    }

    #[no_mangle]
    pub extern "C" fn length(&self) -> f64 {
        self.length_squared().sqrt()
    }

    #[no_mangle]
    pub extern "C" fn length_squared(&self) -> f64 {
        self.vector3.iter().map(|x| x * x).sum()
    }

    #[no_mangle]
    pub extern "C" fn near_zero(&self) -> bool {
        let s = 1e-8;
        self[0].abs() < s && self[1].abs() < s && self[2].abs() < s
    }

    #[no_mangle]
    pub extern "C" fn show(&self) {
        eprintln!("{:?}", self);
    }

    #[no_mangle]
    pub extern "C" fn eq_abi(v1: &Self, v2: &Self) -> bool {
        *v1 == * v2
    }

    #[no_mangle]
    pub extern "C" fn neg_abi(v1: &Self, out: &mut Self) {
        out.vector3 = (-*v1).vector3
    }

    #[no_mangle]
    pub extern "C" fn index_abi(&self, index: usize) -> f64 {
        self[index]
    }

    #[no_mangle]
    pub extern "C" fn add_assign_abi(&mut self,  other: &Self) {
        *self += *other
    }

    #[no_mangle]
    pub extern "C" fn mul_assign_abi(&mut self, other: f64) {
        *self *= other
    }

    #[no_mangle]
    pub extern "C" fn div_assign_abi(&mut self, other: f64) {
        *self /= other
    }

    #[no_mangle]
    pub extern "C" fn add_abi(v1: &Self, v2: &Self, out: &mut Self) {
        out.vector3 = (*v1 + *v2).vector3
    }

    #[no_mangle]
    pub extern "C" fn sub_abi(v1: &Self, v2: &Self, out: &mut Self) {
        out.vector3 = (*v1 - *v2).vector3
    }

    #[no_mangle]
    pub extern "C" fn mul_abi(v1: &Self, v2: &Self, out: &mut Self) {
        out.vector3 = (*v1 * *v2).vector3
    }

    #[no_mangle]
    pub extern "C" fn mul_abi_double(v1: &Self, t: f64, out: &mut Self) {
        out.vector3 = (*v1 * t).vector3
    }

    #[no_mangle]
    pub extern "C" fn div_abi(v1: &Self, t: f64, out: &mut Self) {
        out.vector3 = (*v1 / t).vector3
    }

    #[no_mangle]
    pub extern "C" fn dot(u: &Vec3, v: &Vec3) -> f64 {
        u[0] * v[0] + u[1] * v[1] + u[2] * v[2]
    }

    #[no_mangle]
    pub extern "C" fn cross_abi(v1: &Self, v2: &Self, out: &mut Self) {
        out.vector3 = Self::cross(v1, v2).vector3
    }

    #[no_mangle]
    pub extern "C" fn unit_vector_abi(v1: &Self, out: &mut Self) {
        out.vector3 = Self::unit_vector(*v1).vector3
    }

    #[no_mangle]
    pub extern "C" fn refract_abi(uv: &Self, n: &Self, etai_over_etat: f64, out: &mut Self) {
        out.vector3 = Self::refract(uv, n, etai_over_etat).vector3
    }

    #[no_mangle]
    pub extern "C" fn reflect_abi(v: &Self, n: &Self, out: &mut Self) {
        out.vector3 = Self::reflect(v, n).vector3
    }
}

impl Vec3 {
    pub fn refract(uv: &Vec3, n: &Vec3, etai_over_etat: f64) -> Vec3 {
        let cos_theta = Self::dot(&(-*uv), n).min(1.0);
        let r_out_perp = etai_over_etat * (*uv + cos_theta * (*n));
        let r_out_parallel = -(1.0 - r_out_perp.length_squared()).abs().sqrt() * (*n);
        r_out_perp + r_out_parallel
    }

    pub fn unit_vector(v: Vec3) -> Vec3 {
        v / v.length()
    }
    
    pub fn reflect(v: &Vec3, n: &Vec3) -> Vec3 {
        *v - 2.0 * Self::dot(v, n) * (*n)
    }
    
    pub fn cross(u: &Vec3, v: &Vec3) -> Vec3 {
        Vec3 {
            vector3: [
                u[1] * v[2] - u[2] * v[1],
                u[2] * v[0] - u[0] * v[2],
                u[0] * v[1] - u[1] * v[0],
            ],
        }
    }
}

impl Add for Vec3 {
    type Output = Self;

    fn add(self, other: Self) -> Self {
        Self {
            vector3: [
                self.vector3[0] + other.vector3[0],
                self.vector3[1] + other.vector3[1],
                self.vector3[2] + other.vector3[2],
            ],
        }
    }
}

impl Sub for Vec3 {
    type Output = Self;

    fn sub(self, other: Self) -> Self {
        Self {
            vector3: [
                self.vector3[0] - other.vector3[0],
                self.vector3[1] - other.vector3[1],
                self.vector3[2] - other.vector3[2],
            ],
        }
    }
}

impl Mul for Vec3 {
    type Output = Self;

    fn mul(self, other: Self) -> Self {
        Self {
            vector3: [
                self.vector3[0] * other.vector3[0],
                self.vector3[1] * other.vector3[1],
                self.vector3[2] * other.vector3[2],
            ],
        }
    }
}

impl Mul<f64> for Vec3 {
    type Output = Self;

    fn mul(self, other: f64) -> Self {
        Self {
            vector3: [
                self.vector3[0] * other,
                self.vector3[1] * other,
                self.vector3[2] * other,
            ],
        }
    }
}

impl Mul<Vec3> for f64 {
    type Output = Vec3;

    fn mul(self, other: Vec3) -> Vec3 {
        Vec3 {
            vector3: [
                self * other.vector3[0],
                self * other.vector3[1],
                self * other.vector3[2],
            ],
        }
    }
}

impl Div<f64> for Vec3 {
    type Output = Self;

    fn div(self, other: f64) -> Self {
        self * (1.0 / other)
    }
}

impl Neg for Vec3 {
    type Output = Self;

    fn neg(self) -> Self {
        Self {
            vector3: [-self.vector3[0], -self.vector3[1], -self.vector3[2]],
        }
    }
}

impl AddAssign for Vec3 {
    fn add_assign(&mut self, other: Self) {
        self.vector3[0] += other.vector3[0];
        self.vector3[1] += other.vector3[1];
        self.vector3[2] += other.vector3[2];
    }
}

impl MulAssign<f64> for Vec3 {
    fn mul_assign(&mut self, other: f64) {
        self.vector3[0] *= other;
        self.vector3[1] *= other;
        self.vector3[2] *= other;
    }
}

impl DivAssign<f64> for Vec3 {
    fn div_assign(&mut self, other: f64) {
        self.vector3[0] /= other;
        self.vector3[1] /= other;
        self.vector3[2] /= other;
    }
}

impl Index<usize> for Vec3 {
    type Output = f64;

    fn index(&self, index: usize) -> &f64 {
        &self.vector3[index]
    }
}

impl IndexMut<usize> for Vec3 {
    fn index_mut(&mut self, index: usize) -> &mut Self::Output {
        match index {
            0 => &mut self.vector3[0],
            1 => &mut self.vector3[1],
            2 => &mut self.vector3[2],
            _ => panic!("Accesing out of bound index"),
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_new_vec3() {
        assert_eq!(
            Vec3::empty_vec(),
            Vec3 {
                vector3: [0.0, 0.0, 0.0]
            }
        );
    }

    #[test]
    fn test_new_with_values() {
        assert_eq!(Vec3::empty_vec(), Vec3::new_with_values(0.0, 0.0, 0.0));
    }

    #[test]
    fn test_xyz() {
        let test_vector = Vec3 {
            vector3: [0.0, 1.0, 2.0],
        };
        assert_eq!(test_vector.x(), 0.0);
        assert_eq!(test_vector.y(), 1.0);
        assert_eq!(test_vector.z(), 2.0);
    }

    #[test]
    fn test_length_squared() {
        let test_vector = Vec3 {
            vector3: [1.0, 1.0, 1.0],
        };
        assert_eq!(test_vector.length_squared(), 3.0);
    }

    #[test]
    fn test_length() {
        let test_vector = Vec3 {
            vector3: [1.0, 1.0, 1.0],
        };
        assert_eq!(test_vector.length(), 3.0f64.sqrt());
    }

    #[test]
    fn test_add_vectors() {
        let test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        let test_vector2 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        assert_eq!(
            test_vector1 + test_vector2,
            Vec3 {
                vector3: [2.0, 4.0, 6.0]
            }
        );
    }

    #[test]
    fn test_mul_vectors() {
        let test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        let test_vector2 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        assert_eq!(
            test_vector1.clone() * test_vector2,
            Vec3 {
                vector3: [1.0, 4.0, 9.0]
            }
        );
        assert_eq!(
            test_vector1.clone() * 2.0,
            Vec3 {
                vector3: [2.0, 4.0, 6.0]
            }
        );
        assert_eq!(
            2.0 * test_vector1.clone(),
            Vec3 {
                vector3: [2.0, 4.0, 6.0]
            }
        );
    }

    #[test]
    fn test_sub_vectors() {
        let test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        let test_vector2 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        assert_eq!(
            test_vector1 - test_vector2,
            Vec3 {
                vector3: [0.0, 0.0, 0.0]
            }
        );
    }

    #[test]
    fn test_div() {
        let test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        assert_eq!(
            test_vector1.clone() / 2.0,
            Vec3 {
                vector3: [0.5, 1.0, 1.5]
            }
        );
    }

    #[test]
    fn test_negation() {
        let test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        let test_vector_pointer = &test_vector1;
        assert_eq!(
            -test_vector1,
            Vec3 {
                vector3: [-1.0, -2.0, -3.0]
            }
        );
        assert_eq!(
            -*test_vector_pointer,
            Vec3 {
                vector3: [-1.0, -2.0, -3.0]
            }
        );
    }

    #[test]
    fn test_add_assign() {
        let mut test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        let test_vector2 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        test_vector1 += test_vector2;
        assert_eq!(
            test_vector1,
            Vec3 {
                vector3: [2.0, 4.0, 6.0]
            }
        );
    }

    #[test]
    fn test_mul_assign() {
        let mut test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        test_vector1 *= 2.0;
        assert_eq!(
            test_vector1,
            Vec3 {
                vector3: [2.0, 4.0, 6.0]
            }
        );
    }

    #[test]
    fn test_div_assign() {
        let mut test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        test_vector1 /= 2.0;
        assert_eq!(
            test_vector1,
            Vec3 {
                vector3: [0.5, 1.0, 1.5]
            }
        );
    }

    #[test]
    fn test_index() {
        let test_vector1 = Vec3 {
            vector3: [1.0, 2.0, 3.0],
        };
        assert_eq!(test_vector1[0], 1.0);
        assert_eq!(test_vector1[1], 2.0);
        assert_eq!(test_vector1[2], 3.0);
    }

    #[test]
    fn test_dot() {
        let test_vector = Vec3 {
            vector3: [1f64, 1f64, 1f64],
        };
        assert_eq!(Vec3::dot(&test_vector, &test_vector), 3f64);
        let test_vector2 = Vec3 {
            vector3: [1f64, 2f64, 3f64],
        };
        assert_eq!(Vec3::dot(&test_vector, &test_vector2), 6f64);
    }

    #[test]
    fn test_cross_itself() {
        let test_vector = Vec3 {
            vector3: [1f64, 1f64, 1f64],
        };
        let test_vector2 = Vec3 {
            vector3: [1f64, 1f64, 1f64],
        };
        let zero_vector = Vec3 {
            vector3: [0f64, 0f64, 0f64],
        };
        assert_eq!(Vec3::cross(&test_vector, &test_vector2), zero_vector);
    }

    #[test]
    fn test_cross_orthogonal() {
        let x_vector = Vec3 {
            vector3: [1f64, 0f64, 0f64],
        };
        let y_vector2 = Vec3 {
            vector3: [0f64, 1f64, 0f64],
        };
        let z_vector = Vec3 {
            vector3: [0f64, 0f64, 1f64],
        };
        assert_eq!(Vec3::cross(&x_vector, &y_vector2), z_vector);
    }

    #[test]
    fn test_unit_vector() {
        let test_vector = Vec3 {
            vector3: [1f64, 1f64, 1f64],
        };
        debug_assert_eq!(Vec3::unit_vector(test_vector), test_vector / 3.0f64.sqrt());
    }
}